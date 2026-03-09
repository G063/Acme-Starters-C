<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="any.invention.list.label.ticker" path="ticker" width="20%"/>
    <acme:list-column code="any.invention.list.label.name" path="name" width="40%"/>
    <acme:list-column code="any.invention.list.label.cost" path="cost" width="15%"/>
    <acme:list-column code="any.invention.list.label.inventor" path="inventor.userAccount.identity.name" width="25%"/>
    
    <acme:list-hidden path="description"/>
    <acme:list-hidden path="inventor.bio"/>
</acme:list>

<acme:return/>