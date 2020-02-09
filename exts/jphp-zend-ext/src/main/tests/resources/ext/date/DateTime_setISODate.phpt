--TEST--
DateTime::setISODate with wide range of weeks
--FILE--
<?php
date_default_timezone_set('Europe/Berlin');
$start = -100;
$end = 100;

for($i = $start; $i <= $end; $i++) {
    $date = new DateTime('18-01-2009 00:00:00');
    $date->setISODate(2009, $i, 1);

    printf("Week #%02d: %s\n", $i, $date->format('Y-m-d'));
}
?>
--EXPECT--
Week #-100: 2007-01-22
Week #-99: 2007-01-29
Week #-98: 2007-02-05
Week #-97: 2007-02-12
Week #-96: 2007-02-19
Week #-95: 2007-02-26
Week #-94: 2007-03-05
Week #-93: 2007-03-12
Week #-92: 2007-03-19
Week #-91: 2007-03-26
Week #-90: 2007-04-02
Week #-89: 2007-04-09
Week #-88: 2007-04-16
Week #-87: 2007-04-23
Week #-86: 2007-04-30
Week #-85: 2007-05-07
Week #-84: 2007-05-14
Week #-83: 2007-05-21
Week #-82: 2007-05-28
Week #-81: 2007-06-04
Week #-80: 2007-06-11
Week #-79: 2007-06-18
Week #-78: 2007-06-25
Week #-77: 2007-07-02
Week #-76: 2007-07-09
Week #-75: 2007-07-16
Week #-74: 2007-07-23
Week #-73: 2007-07-30
Week #-72: 2007-08-06
Week #-71: 2007-08-13
Week #-70: 2007-08-20
Week #-69: 2007-08-27
Week #-68: 2007-09-03
Week #-67: 2007-09-10
Week #-66: 2007-09-17
Week #-65: 2007-09-24
Week #-64: 2007-10-01
Week #-63: 2007-10-08
Week #-62: 2007-10-15
Week #-61: 2007-10-22
Week #-60: 2007-10-29
Week #-59: 2007-11-05
Week #-58: 2007-11-12
Week #-57: 2007-11-19
Week #-56: 2007-11-26
Week #-55: 2007-12-03
Week #-54: 2007-12-10
Week #-53: 2007-12-17
Week #-52: 2007-12-24
Week #-51: 2007-12-31
Week #-50: 2008-01-07
Week #-49: 2008-01-14
Week #-48: 2008-01-21
Week #-47: 2008-01-28
Week #-46: 2008-02-04
Week #-45: 2008-02-11
Week #-44: 2008-02-18
Week #-43: 2008-02-25
Week #-42: 2008-03-03
Week #-41: 2008-03-10
Week #-40: 2008-03-17
Week #-39: 2008-03-24
Week #-38: 2008-03-31
Week #-37: 2008-04-07
Week #-36: 2008-04-14
Week #-35: 2008-04-21
Week #-34: 2008-04-28
Week #-33: 2008-05-05
Week #-32: 2008-05-12
Week #-31: 2008-05-19
Week #-30: 2008-05-26
Week #-29: 2008-06-02
Week #-28: 2008-06-09
Week #-27: 2008-06-16
Week #-26: 2008-06-23
Week #-25: 2008-06-30
Week #-24: 2008-07-07
Week #-23: 2008-07-14
Week #-22: 2008-07-21
Week #-21: 2008-07-28
Week #-20: 2008-08-04
Week #-19: 2008-08-11
Week #-18: 2008-08-18
Week #-17: 2008-08-25
Week #-16: 2008-09-01
Week #-15: 2008-09-08
Week #-14: 2008-09-15
Week #-13: 2008-09-22
Week #-12: 2008-09-29
Week #-11: 2008-10-06
Week #-10: 2008-10-13
Week #-9: 2008-10-20
Week #-8: 2008-10-27
Week #-7: 2008-11-03
Week #-6: 2008-11-10
Week #-5: 2008-11-17
Week #-4: 2008-11-24
Week #-3: 2008-12-01
Week #-2: 2008-12-08
Week #-1: 2008-12-15
Week #00: 2008-12-22
Week #01: 2008-12-29
Week #02: 2009-01-05
Week #03: 2009-01-12
Week #04: 2009-01-19
Week #05: 2009-01-26
Week #06: 2009-02-02
Week #07: 2009-02-09
Week #08: 2009-02-16
Week #09: 2009-02-23
Week #10: 2009-03-02
Week #11: 2009-03-09
Week #12: 2009-03-16
Week #13: 2009-03-23
Week #14: 2009-03-30
Week #15: 2009-04-06
Week #16: 2009-04-13
Week #17: 2009-04-20
Week #18: 2009-04-27
Week #19: 2009-05-04
Week #20: 2009-05-11
Week #21: 2009-05-18
Week #22: 2009-05-25
Week #23: 2009-06-01
Week #24: 2009-06-08
Week #25: 2009-06-15
Week #26: 2009-06-22
Week #27: 2009-06-29
Week #28: 2009-07-06
Week #29: 2009-07-13
Week #30: 2009-07-20
Week #31: 2009-07-27
Week #32: 2009-08-03
Week #33: 2009-08-10
Week #34: 2009-08-17
Week #35: 2009-08-24
Week #36: 2009-08-31
Week #37: 2009-09-07
Week #38: 2009-09-14
Week #39: 2009-09-21
Week #40: 2009-09-28
Week #41: 2009-10-05
Week #42: 2009-10-12
Week #43: 2009-10-19
Week #44: 2009-10-26
Week #45: 2009-11-02
Week #46: 2009-11-09
Week #47: 2009-11-16
Week #48: 2009-11-23
Week #49: 2009-11-30
Week #50: 2009-12-07
Week #51: 2009-12-14
Week #52: 2009-12-21
Week #53: 2009-12-28
Week #54: 2010-01-04
Week #55: 2010-01-11
Week #56: 2010-01-18
Week #57: 2010-01-25
Week #58: 2010-02-01
Week #59: 2010-02-08
Week #60: 2010-02-15
Week #61: 2010-02-22
Week #62: 2010-03-01
Week #63: 2010-03-08
Week #64: 2010-03-15
Week #65: 2010-03-22
Week #66: 2010-03-29
Week #67: 2010-04-05
Week #68: 2010-04-12
Week #69: 2010-04-19
Week #70: 2010-04-26
Week #71: 2010-05-03
Week #72: 2010-05-10
Week #73: 2010-05-17
Week #74: 2010-05-24
Week #75: 2010-05-31
Week #76: 2010-06-07
Week #77: 2010-06-14
Week #78: 2010-06-21
Week #79: 2010-06-28
Week #80: 2010-07-05
Week #81: 2010-07-12
Week #82: 2010-07-19
Week #83: 2010-07-26
Week #84: 2010-08-02
Week #85: 2010-08-09
Week #86: 2010-08-16
Week #87: 2010-08-23
Week #88: 2010-08-30
Week #89: 2010-09-06
Week #90: 2010-09-13
Week #91: 2010-09-20
Week #92: 2010-09-27
Week #93: 2010-10-04
Week #94: 2010-10-11
Week #95: 2010-10-18
Week #96: 2010-10-25
Week #97: 2010-11-01
Week #98: 2010-11-08
Week #99: 2010-11-15
Week #100: 2010-11-22