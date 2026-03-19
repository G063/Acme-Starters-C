<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:set var="canEdit" value="${_command == 'create' || draftMode == true}"/>
<jstl:set var="isReadonly" value="${!canEdit}"/>

<acme:form>
	<acme:form-textbox code="auditor.audit-report.form.label.ticker" path="ticker" placeholder="XX00-XXXXXXXXXX" readonly="${isReadonly}"/>
	<acme:form-textbox code="auditor.audit-report.form.label.name" path="name" readonly="${isReadonly}"/>
	<acme:form-textarea code="auditor.audit-report.form.label.description" path="description" readonly="${isReadonly}"/>
	<acme:form-moment code="auditor.audit-report.form.label.start-moment" path="startMoment" readonly="${isReadonly}"/>
	<acme:form-moment code="auditor.audit-report.form.label.end-moment" path="endMoment" readonly="${isReadonly}"/>
	<acme:form-url code="auditor.audit-report.form.label.more-info" path="moreInfo" readonly="${isReadonly}"/>
	<acme:form-select code="auditor.audit-report.form.label.draft-mode" path="draftMode" choices="${draftModes}" readonly="true"/>

	<jstl:if test="${_command != 'create'}">
		<acme:form-double code="auditor.audit-report.form.label.months-active" path="monthsActive" readonly="true"/>
		<acme:form-integer code="auditor.audit-report.form.label.hours" path="hours" readonly="true"/>
	</jstl:if>

	<jstl:choose>
		<jstl:when test="${canEdit && _command != 'create'}">
			<acme:button code="auditor.audit-report.form.button.audit-sections" action="/auditor/audit-section/list?auditReportId=${id}"/>
			<acme:submit code="auditor.audit-report.form.button.update" action="/auditor/audit-report/update"/>
			<acme:submit code="auditor.audit-report.form.button.delete" action="/auditor/audit-report/delete"/>
			<acme:submit code="auditor.audit-report.form.button.publish" action="/auditor/audit-report/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.audit-report.form.button.create" action="/auditor/audit-report/create"/>
		</jstl:when>
		<jstl:otherwise>
			<acme:button code="auditor.audit-report.form.button.audit-sections" action="/auditor/audit-section/list?auditReportId=${id}"/>
		</jstl:otherwise>
	</jstl:choose>
</acme:form>
