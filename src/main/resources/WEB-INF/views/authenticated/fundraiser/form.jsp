<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:form-textbox code="authenticated.fundraiser.label.bank" path="bank"/>
    
    <acme:form-textarea code="authenticated.fundraiser.label.statement" path="statement"/>

    <acme:submit code="authenticated.fundraiser.button.save" action="/authenticated/fundraiser/${command}"/>
    
    <acme:return/>
</acme:form>