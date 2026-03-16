<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="fundraiser.tactic.list.label.name" path="name" width="30%"/>
    <acme:list-column code="fundraiser.tactic.list.label.expectedPercentage" path="expectedPercentage" width="20%"/>
    <acme:list-column code="fundraiser.tactic.list.label.kind" path="kind" width="20%"/>
    <acme:list-hidden path="notes"/>
</acme:list>

<acme:return/>

<acme:button action="/fundraiser/tactic/create?strategyId=${param.strategyId}" code="fundraiser.tactic.list.button.create"/>