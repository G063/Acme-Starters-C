<%@page language="java"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.milestone.label.title" path="title" readonly="true"/>
	<acme:form-textbox code="any.milestone.label.achievement" path="achievement" readonly="true"/>
	<acme:form-textbox code="any.milestone.label.effort" path="effort" readonly="true"/>
	<acme:form-textarea code="any.milestone.label.kind" path="kind" readonly="true"/>

	<hr/>

	<h3><acme:print code="any.milestone.label.campaign-info"/></h3>

	<acme:form-textbox code="any.milestone.label.campaign.ticker" path="campaign.ticker" readonly="true"/>

	<acme:return/>
</acme:form>