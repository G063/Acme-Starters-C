<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="fundraiser.strategy.list.label.ticker" path="ticker" width="15%"/>
    <acme:list-column code="fundraiser.strategy.list.label.name" path="name" width="35%"/>
    <acme:list-column code="fundraiser.strategy.list.label.draft-mode" path="draftMode" width="15%"/>
    
    <acme:list-hidden path="description"/>
</acme:list>

<acme:button action="/fundraiser/strategy/create" code="fundraiser.strategy.list.button.create"/>