<%--
- form.jsp
-
- Copyright (C) 2012-2026 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
	<acme:form-textbox code="any.audit-report.form.label.ticker" path="ticker"/>
	<acme:form-textbox code="any.audit-report.form.label.name" path="name"/>
	<acme:form-textarea code="any.audit-report.form.label.description" path="description"/>
	<acme:form-moment code="any.audit-report.form.label.startMoment" path="startMoment"/>
	<acme:form-moment code="any.audit-report.form.label.endMoment" path="endMoment"/>
	<acme:form-textbox code="any.audit-report.form.label.monthsActive" path="monthsActive"/>
	<acme:form-textbox code="any.audit-report.form.label.hours" path="hours"/>
	<acme:form-url code="any.audit-report.form.label.moreInfo" path="moreInfo"/>

	<hr/>

	<h3><acme:print code="any.audit-report.form.label.auditorProfile"/></h3>

	<acme:form-textbox code="any.audit-report.form.label.auditorName" path="auditor.userAccount.identity.name"/>
	<acme:form-textbox code="any.audit-report.form.label.auditorSurname" path="auditor.userAccount.identity.surname"/>
	<acme:form-email code="any.audit-report.form.label.auditorEmail" path="auditor.userAccount.identity.email"/>
	<acme:form-textbox code="any.audit-report.form.label.auditorFirm" path="auditor.firm"/>
	<acme:form-textbox code="any.audit-report.form.label.auditorHighlights" path="auditor.highlights"/>
	<acme:form-checkbox code="any.audit-report.form.label.auditorSolicitor" path="auditor.solicitor"/>

	<acme:button code="any.audit-report.form.button.auditSections" action="/any/audit-section/list?auditReportId=${id}"/>
</acme:form>
