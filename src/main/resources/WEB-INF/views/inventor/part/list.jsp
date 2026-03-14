<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <%-- Atributos de Part según tu UML --%>
    <acme:list-column code="inventor.part.list.label.name" path="name" width="25%"/>
    <acme:list-column code="inventor.part.list.label.cost" path="cost" width="15%"/>
    <acme:list-column code="inventor.part.list.label.kind" path="kind" width="15%"/>
    
    <%-- Enlaces de gestión (suponiendo que los generas en el unbind del Service) --%>
    <acme:list-column code="inventor.part.list.label.description" path="description" width="15%"/>

    <%-- Campos que no quieres mostrar en la tabla pero deben estar en el modelo --%>
    <acme:list-hidden path="description"/>
</acme:list>

<%-- Botones de navegación --%>
<acme:return/>
<acme:button action="/inventor/part/create?inventionId=${inventionId}" code="inventor.part.list.button.create"/>