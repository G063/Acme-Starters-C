<%@page language="java"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.milestone.label.title" path="title" readonly="true"/>
	<acme:form-textbox code="any.milestone.label.achievement" path="achievements" readonly="true"/>
	<acme:form-textbox code="any.milestone.label.effort" path="effort" readonly="true"/>
	<acme:form-textarea code="any.milestone.label.kind" path="kind" readonly="true"/>
</acme:form>