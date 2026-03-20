<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.spokesperson.form.label.name" path="userAccount.identity.name" readonly="true"/>
	<acme:form-textbox code="any.spokesperson.form.label.surname" path="userAccount.identity.surname" readonly="true"/>
	<acme:form-email code="any.spokesperson.form.label.email" path="userAccount.identity.email" readonly="true"/>
	<acme:form-textbox code="any.spokesperson.form.label.cv" path="cv" readonly="true"/>
	<acme:form-textbox code="any.spokesperson.form.label.achievements" path="achievements" readonly="true"/>
	<acme:form-checkbox code="any.spokesperson.form.label.licensed" path="licensed" readonly="true"/>
</acme:form>
