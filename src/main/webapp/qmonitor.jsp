<%@ page session="false" %>
<%@page import="com.qunar.flight.qmonitor.QMonitor" %>
<%@page import="java.util.Map.Entry"%>
<%@ page contentType="text/plain;charset=UTF-8" language="java" %>
<%
if (QMonitor.getValues()!=null){
	for (Entry<String, Long> entry : QMonitor.getValues().entrySet()) {
		String name = entry.getKey();
		Long value = entry.getValue();
		out.append(name + "=" + value + "\n");
	}
}
%>
