<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:set var="canEdit" value="${_command == 'create' || (auditReportDraftMode == true && (_command == 'show' || _command == 'update'))}"/>
<jstl:set var="isReadonly" value="${!canEdit}"/>

<acme:form>
	<acme:form-textbox code="auditor.audit-section.form.label.name" path="name" readonly="${isReadonly}"/>
	<acme:form-textarea code="auditor.audit-section.form.label.notes" path="notes" readonly="${isReadonly}"/>
	<acme:form-integer code="auditor.audit-section.form.label.hours" path="hours" readonly="${isReadonly}"/>

	<jstl:if test="${canEdit}">
		<acme:form-select code="auditor.audit-section.form.label.kind" path="kind" choices="${kinds}"/>
	</jstl:if>
	<jstl:if test="${!canEdit}">
		<acme:form-textbox code="auditor.audit-section.form.label.kind" path="kind" readonly="true"/>
	</jstl:if>

	<jstl:choose>
		<jstl:when test="${canEdit && _command != 'create'}">
			<acme:submit code="auditor.audit-section.form.button.update" action="/auditor/audit-section/update"/>
			<acme:submit code="auditor.audit-section.form.button.delete" action="/auditor/audit-section/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.audit-section.form.button.create" action="/auditor/audit-section/create?auditReportId=${auditReportId}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
