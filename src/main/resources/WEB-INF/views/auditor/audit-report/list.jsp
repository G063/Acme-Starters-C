<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="auditor.audit-report.list.label.ticker" path="ticker" width="15%"/>
	<acme:list-column code="auditor.audit-report.list.label.name" path="name" width="30%"/>
	<acme:list-column code="auditor.audit-report.list.label.start-moment" path="startMoment" width="15%"/>
	<acme:list-column code="auditor.audit-report.list.label.end-moment" path="endMoment" width="15%"/>
	<acme:list-column code="auditor.audit-report.list.label.draft-mode" path="draftMode" width="10%"/>
</acme:list>

<acme:button code="auditor.audit-report.list.button.create" action="/auditor/audit-report/create"/>
