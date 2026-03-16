<%@page language="java"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.spokesperson.form.label.name" path="identity.name"/>
	<acme:form-textbox code="any.spokesperson.form.label.surname" path="identity.surname"/>
	<acme:form-textbox code="any.spokesperson.form.label.cv" path="cv"/>
	<acme:form-textbox code="any.spokesperson.form.label.achievements" path="achievements"/>
	<acme:form-textarea code="any.spokesperson.form.label.licensed" path="licensed"/>
</acme:form>
