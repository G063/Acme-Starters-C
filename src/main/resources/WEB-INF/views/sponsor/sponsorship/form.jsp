<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:set var="readonly" value="${_command == 'show' || _command == 'delete' || _command == 'publish'}"/>

<acme:form>
	<acme:form-textbox code="sponsor.sponsorship.form.label.ticker" path="ticker" readonly="${readonly}"/>
	<acme:form-textbox code="sponsor.sponsorship.form.label.name" path="name" readonly="${readonly}"/>
	<acme:form-textarea code="sponsor.sponsorship.form.label.description" path="description" readonly="${readonly}"/>
	<acme:form-moment code="sponsor.sponsorship.form.label.start-moment" path="startMoment" readonly="${readonly}"/>
	<acme:form-moment code="sponsor.sponsorship.form.label.end-moment" path="endMoment" readonly="${readonly}"/>
	<acme:form-url code="sponsor.sponsorship.form.label.more-info" path="moreInfo" readonly="${readonly}"/>
	<acme:form-select code="sponsor.sponsorship.form.label.draft-mode" path="draftMode" choices="${draftModes}" readonly="true"/>

	<jstl:if test="${_command != 'create'}">
		<acme:form-double code="sponsor.sponsorship.form.label.months-active" path="monthsActive" readonly="true"/>
		<acme:form-money code="sponsor.sponsorship.form.label.total-money" path="totalMoney" readonly="true"/>
	</jstl:if>

	<jstl:if test="${_command == 'create'}">
		<acme:submit code="sponsor.sponsorship.form.button.create" action="/sponsor/sponsorship/create"/>
	</jstl:if>
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="sponsor.sponsorship.form.button.update" action="/sponsor/sponsorship/update"/>
	</jstl:if>
	<jstl:if test="${_command == 'show'}">
		<acme:button code="sponsor.sponsorship.form.button.donations" action="/sponsor/donation/list?sponsorshipId=${id}"/>
		<jstl:if test="${draftMode == true}">
			<acme:button code="sponsor.sponsorship.form.button.update" action="/sponsor/sponsorship/update?id=${id}"/>
			<acme:button code="sponsor.sponsorship.form.button.delete" action="/sponsor/sponsorship/delete"/>
			<acme:button code="sponsor.sponsorship.form.button.publish" action="/sponsor/sponsorship/publish"/>
		</jstl:if>
	</jstl:if>
</acme:form>
