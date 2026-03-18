<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:form-textbox code="any.inventor.form.label.name" path="userAccount.identity.name" readonly="true"/>
    <acme:form-textbox code="any.inventor.form.label.surname" path="userAccount.identity.surname" readonly="true"/>
    <acme:form-email code="any.inventor.form.label.email" path="userAccount.identity.email" readonly="true"/>

    <hr/>

    <acme:form-textarea code="any.inventor.form.label.bio" path="bio" readonly="true"/>
    <acme:form-textbox code="any.inventor.form.label.keyWords" path="keyWords" readonly="true"/>
    <acme:form-checkbox code="any.inventor.form.label.licensed" path="licensed" readonly="true"/>
</acme:form>