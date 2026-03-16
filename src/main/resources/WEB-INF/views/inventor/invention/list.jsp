<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="inventor.invention.list.label.ticker" path="ticker" width="15%"/>
    <acme:list-column code="inventor.invention.list.label.name" path="name" width="35%"/>
    <acme:list-column code="inventor.invention.list.label.cost" path="cost" width="15%"/>
    <acme:list-column code="inventor.invention.list.label.draft-mode" path="draftMode" width="15%"/>
    
    <acme:list-hidden path="description"/>
</acme:list>

<acme:return/>
<acme:button action="/inventor/invention/create" code="inventor.invention.list.button.create"/>