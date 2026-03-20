<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="auditor.audit-section.list.label.name" path="name" width="30%"/>
	<acme:list-column code="auditor.audit-section.list.label.kind" path="kind" width="20%"/>
	<acme:list-column code="auditor.audit-section.list.label.hours" path="hours" width="15%"/>
	<acme:list-column code="auditor.audit-section.list.label.notes" path="notes" width="35%"/>
</acme:list>

<jstl:if test="${auditReportDraftMode}">
	<acme:button code="auditor.audit-section.list.button.create" action="/auditor/audit-section/create?auditReportId=${auditReportId}"/>
</jstl:if>
