<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:set var="readonly" value="${_command == 'show' || _command == 'delete'}"/>

<acme:form>
	<acme:form-textbox code="sponsor.donation.form.label.name" path="name" readonly="${readonly}"/>
	<acme:form-textarea code="sponsor.donation.form.label.notes" path="notes" readonly="${readonly}"/>
	<acme:form-money code="sponsor.donation.form.label.money" path="money" readonly="${readonly}"/>

	<jstl:if test="${_command == 'create' || _command == 'update'}">
		<acme:form-select code="sponsor.donation.form.label.kind" path="kind" choices="${kinds}"/>
	</jstl:if>
	<jstl:if test="${_command == 'show' || _command == 'delete'}">
		<acme:form-textbox code="sponsor.donation.form.label.kind" path="kind" readonly="true"/>
	</jstl:if>

	<jstl:if test="${_command == 'create'}">
		<acme:submit code="sponsor.donation.form.button.create" action="/sponsor/donation/create?sponsorshipId=${sponsorshipId}"/>
	</jstl:if>
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="sponsor.donation.form.button.update" action="/sponsor/donation/update"/>
	</jstl:if>
	<jstl:if test="${_command == 'delete'}">
		<acme:submit code="sponsor.donation.form.button.delete" action="/sponsor/donation/delete"/>
	</jstl:if>

	<jstl:if test="${_command == 'show'}">
		<jstl:if test="${sponsorshipDraftMode}">
			<acme:button code="sponsor.donation.form.button.update" action="/sponsor/donation/update?id=${id}"/>
			<acme:button code="sponsor.donation.form.button.delete" action="/sponsor/donation/delete?id=${id}"/>
		</jstl:if>
	</jstl:if>
</acme:form>
