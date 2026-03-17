<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.tactic.label.name" path="name" readonly="true"/>
	<acme:form-textbox code="any.tactic.label.kind" path="kind" readonly="true"/>
	<acme:form-textbox code="any.tactic.label.expectedPercentage" path="expectedPercentage" readonly="true"/>
	<acme:form-textarea code="any.tactic.label.notes" path="notes" readonly="true"/>

	<hr/>

	<h3><acme:print code="any.tactic.label.strategy-info"/></h3>

	<acme:form-textbox code="any.tactic.label.strategy.ticker" path="strategy.ticker" readonly="true"/>

</acme:form>