package com.greenpepper.report;

public class XmlReportMother
{
    public static String simpleResultString() throws Exception
    {
        return
            "<documents>\n"
            + "<global-exception><![CDATA[my exception]]></global-exception>\n"
            + "<document>\n"
			+ "    <name><![CDATA[Calculator]]></name>\n"
			+ "    <external-link><![CDATA[http://www.greenpeppersoftware.com/confluence/display/GREENPEPPERDEMO/Calculator]]></external-link>\n" 
			+ "    <time-statistics>\n"
			+ "        <execution>6</execution>\n"
			+ "        <total>7</total>\n"
			+ "    </time-statistics>\n"
            + "    <statistics>\n"
            + "        <success>1</success>\n"
            + "        <failure>2</failure>\n"
            + "        <error>3</error>\n"
            + "        <ignored>4</ignored>\n"
            + "        <annotation>5</annotation>\n"
            + "    </statistics>\n"
            + "    <results><![CDATA[<table><tr><td>my results</td></tr></table>]]></results>\n"
            + "    <exception><![CDATA[my exception]]></exception>\n"
			+ "    <sections>\n"
			+ "        <section>unix</section>\n"
			+ "    </sections>\n"
            + "</document>\n"
            + "</documents>\n";
    }

    public static XmlReport simpleResults() throws Exception
    {
        return XmlReport.parse( simpleResultString() );
    }

}

