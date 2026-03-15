<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.part.label.name" path="name" readonly="true"/>
	<acme:form-textbox code="any.part.label.kind" path="kind" readonly="true"/>
	<acme:form-textbox code="any.part.label.cost" path="cost" readonly="true"/>
	<acme:form-textarea code="any.part.label.description" path="description" readonly="true"/>

	<hr/>

	<h3><acme:print code="any.part.label.invention-info"/></h3>

	<acme:form-textbox code="any.part.label.invention.ticker" path="invention.ticker" readonly="true"/>

	<acme:return/>
</acme:form>