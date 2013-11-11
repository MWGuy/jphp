package ru.regenix.jphp.compiler.jvm.stetament;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import ru.regenix.jphp.compiler.common.misc.LocalVariable;
import ru.regenix.jphp.compiler.common.misc.StackItem;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.stmt.ArgumentStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.MethodStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ReturnStmtToken;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.reflection.MethodEntity;
import ru.regenix.jphp.runtime.reflection.ParameterEntity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

public class MethodStmtCompiler extends StmtCompiler<MethodEntity> {

    public final ClassStmtCompiler clazz;
    public final MethodStmtToken method;

    private Stack<StackItem> stack = new Stack<StackItem>();

    private int stackSize = 0;
    private int stackMaxSize = 0;

    private Map<String, LocalVariable> localVariables;
    protected MethodVisitor mv;

    public MethodStmtCompiler(ClassStmtCompiler clazz, MethodVisitor mv, String methodName) {
        super(clazz.getCompiler());
        this.clazz = clazz;
        this.mv = mv;
        this.method = null;
        this.localVariables = new LinkedHashMap<String, LocalVariable>();

        entity = new MethodEntity(getCompiler().getContext());
        entity.setClazz(clazz.entity);
        entity.setName(methodName);
    }

    public MethodStmtCompiler(ClassStmtCompiler clazz, MethodStmtToken method) {
        super(clazz.getCompiler());
        this.clazz = clazz;
        this.method = method;
        this.localVariables = new LinkedHashMap<String, LocalVariable>();

        entity = new MethodEntity(getCompiler().getContext());
        entity.setClazz(clazz.entity);
        entity.setName(method.getName().getName());
    }

    public Map<String, LocalVariable> getLocalVariables() {
        return localVariables;
    }

    void push(StackItem item){
        stack.push(item);
        stackSize += item.size;
        if (stackMaxSize < stackSize)
            stackMaxSize = stackSize;
    }

    int getStackSize(){
        return stackSize;
    }

    StackItem pop(){
        StackItem item = stack.pop();
        stackSize -= item.size;
        return item;
    }

    void popAll(){
        stackSize = 0;
        stack.clear();
    }

    StackItem peek(){
        return stack.peek();
    }

    LocalVariable addLocalVariable(String variable, Label label, Class clazz){
        LocalVariable result;
        localVariables.put(
                variable,
                result = new LocalVariable(variable, localVariables.size(), label, clazz)
        );
        return result;
    }

    LocalVariable addLocalVariable(String variable, Label label){
        return addLocalVariable(variable, label, Memory.class);
    }

    LocalVariable getLocalVariable(String variable){
        return localVariables.get(variable);
    }

    void writeHeader(){
        if (mv == null){
            int access = 0;
            switch (method.getModifier()){
                case PRIVATE: access += Opcodes.ACC_PRIVATE; break;
                case PROTECTED: access += Opcodes.ACC_PROTECTED; break;
                case PUBLIC: access += Opcodes.ACC_PUBLIC; break;
            }

            if (method.isStatic()) access += Opcodes.ACC_STATIC;
            if (method.isAbstract()) access += Opcodes.ACC_ABSTRACT;
            if (method.isFinal()) access += Opcodes.ACC_FINAL;

            mv = clazz.cw.visitMethod(access, method.getName().getName(),
                    Type.getMethodDescriptor(
                            Type.getType(Memory.class),
                            Type.getType(Environment.class),
                            Type.getType(Memory[].class)
                    ),
                    null, null
            );
        }
        mv.visitCode();

        if (method != null){
            Label label = writeLabel(mv, method.getMeta().getStartLine());

            if (!method.isStatic())
                addLocalVariable("this", label, Object.class);

            addLocalVariable("~env", label, Environment.class); // Environment env
            LocalVariable args = addLocalVariable("~args", label, Memory[].class);  // Memory[] arguments

            int i = 0;
            for(ArgumentStmtToken argument : method.getArguments()){
                LocalVariable local = addLocalVariable(argument.getName().getName(), label, Memory.class);

                ExpressionStmtCompiler expressionCompiler = new ExpressionStmtCompiler(this, null);
                expressionCompiler.writeVarLoad(args.index);
                expressionCompiler.writePushGetFromArray(i, Memory.class);
                expressionCompiler.writeVarStore(local.index, false, false);

                i++;
            }
        } else {
            Label label = writeLabel(mv, clazz.clazz.getMeta().getStartLine());
        }
    }

    void writeFooter(){
        Label endL = new Label();
        mv.visitLabel(endL);

        for(LocalVariable variable : localVariables.values()){
            String description = Type.getDescriptor(variable.getClazz() == null ? Object.class : variable.getClazz());
            if (!method.isStatic() && variable.name.equals("this"))
                description = "L" + clazz.clazz.getFulledName('/') + ";";

            mv.visitLocalVariable(
                    variable.name,
                    description,
                    null,
                    variable.label,
                    endL,
                    variable.index
            );
        }

        mv.visitMaxs(this.stackMaxSize, this.localVariables.size());
        mv.visitEnd();
    }

    @Override
    public MethodEntity compile() {
        if (method != null){
            entity.setAbstract(method.isAbstract());
            entity.setFinal(method.isFinal());
            entity.setStatic(method.isStatic());
            entity.setModifier(method.getModifier());

            ParameterEntity[] parameters = new ParameterEntity[method.getArguments().size()];
            int i = 0;
            for(ArgumentStmtToken argument : method.getArguments()){
                parameters[i] = new ParameterEntity(compiler.getContext());
                parameters[i].setMethod(entity);
                parameters[i].setReference(argument.isReference());
                parameters[i].setClazz(clazz.entity);
                parameters[i].setName(argument.getName().getName());

                if (argument.getValue() != null){
                    ExpressionStmtCompiler expressionCompiler = new ExpressionStmtCompiler(this, null);
                    Memory result = expressionCompiler.writeExpression(argument.getValue(), true, true);
                    if (result == null){
                        unexpectedToken(argument.getValue().getTokens().get(0));
                    }
                    parameters[i].setDefaultValue( result );
                }
                i++;
            }
            entity.setParameters(parameters);
        }

        writeHeader();

        if (method.getBody() != null){
            for(ExprStmtToken instruction : method.getBody().getInstructions()){
                compiler.compileExpression(this, instruction);
            }
        }

        ReturnStmtToken token = new ReturnStmtToken(new TokenMeta("", 0, 0, 0, 0));
        token.setValue(new ExprStmtToken(Token.of("null")));
        compiler.compileExpression(this, new ExprStmtToken(token));

        writeFooter();
        return entity;
    }

}