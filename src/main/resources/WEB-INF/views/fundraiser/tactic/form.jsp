<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:set var="readonly" value="${_command == 'show' || _command == 'delete'}"/>

<acme:form>
	<acme:form-textbox code="fundraiser.tactic.form.label.name" path="name" readonly="${readonly}"/>
	<acme:form-textarea code="fundraiser.tactic.form.label.notes" path="notes" readonly="${readonly}"/>
	<acme:form-textbox code="fundraiser.tactic.form.label.expectedPercentage" path="expectedPercentage" readonly="${readonly}"/>
	
	<jstl:if test="${_command == 'create' || _command == 'update'}">
		<acme:form-select code="fundraiser.tactic.form.label.kind" path="kind" choices="${kinds}"/>
	</jstl:if>
	<jstl:if test="${_command == 'show' || _command == 'delete'}">
		<acme:form-textbox code="fundraiser.tactic.form.label.kind" path="kind" readonly="true"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="fundraiser.tactic.form.button.create" action="/fundraiser/tactic/create?strategyId=${strategyId}"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="fundraiser.tactic.form.button.update" action="/fundraiser/tactic/update"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'show'}">
		<jstl:if test="${strategyDraftMode == true}">
			<acme:button code="fundraiser.tactic.form.button.update" action="/fundraiser/tactic/update?id=${id}"/>
			<acme:submit code="fundraiser.tactic.form.button.delete" action="/fundraiser/tactic/delete"/>
		</jstl:if>
	</jstl:if>
</acme:form>