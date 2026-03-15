<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="inventor.part.list.label.name" path="name" width="25%"/>
    <acme:list-column code="inventor.part.list.label.cost" path="cost" width="15%"/>
    <acme:list-column code="inventor.part.list.label.kind" path="kind" width="15%"/>
    <acme:list-column code="inventor.part.list.label.description" path="description" width="15%"/>
    <acme:list-hidden path="description"/>
</acme:list>

<acme:return/>
<acme:button action="/inventor/part/create?inventionId=${inventionId}" code="inventor.part.list.button.create"/>