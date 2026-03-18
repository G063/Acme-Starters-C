<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:form-textbox code="any.fundraiser.form.label.name" path="userAccount.identity.name" readonly="true"/>
    <acme:form-textbox code="any.fundraiser.form.label.surname" path="userAccount.identity.surname" readonly="true"/>
    <acme:form-email code="any.fundraiser.form.label.email" path="userAccount.identity.email" readonly="true"/>

    <hr/>

    <acme:form-textbox code="any.fundraiser.form.label.bank" path="bank" readonly="true"/>
    <acme:form-textarea code="any.fundraiser.form.label.statement" path="statement" readonly="true"/>

    <br/>

    <acme:button code="any.fundraiser.form.button.strategy" action="/any/strategy/show?id=${strategyId}"/>
</acme:form>