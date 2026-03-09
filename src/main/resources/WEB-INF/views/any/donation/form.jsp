<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.donation.form.label.name" path="name" readonly="true"/>
	<acme:form-textbox code="any.donation.form.label.kind" path="kind" readonly="true"/>
	<acme:form-email code="any.donation.form.label.money" path="money" readonly="true"/>
	<acme:form-textbox code="any.donation.form.label.notes" path="notes" readonly="true"/>
</acme:form>