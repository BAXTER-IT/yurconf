package org.yurconf.demo.om;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author yura
 * @sinceDevelopmentVersion
 */
@XmlType
@XmlEnum(String.class)
public enum GreetingType
{
  HI, HELLO, GOOD_MORNING, GOOD_AFTERNOON, CIAO;
}
