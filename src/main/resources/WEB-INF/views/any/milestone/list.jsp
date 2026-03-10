<%@page language="java"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="any.milestone.list.label.title" path="title" width="25%"/>
    <acme:list-column code="any.milestone.list.label.achievement" path="achievement" width="15%"/>
    <acme:list-column code="any.milestone.list.label.kind" path="kind" width="15%"/>
    <acme:list-column code="any.milestone.list.label.effort" path="effort" width="45%"/>
   
</acme:list>

<acme:return/>