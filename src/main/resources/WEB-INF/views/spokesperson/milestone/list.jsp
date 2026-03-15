<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <%-- Columnas visibles basadas en el modelo Milestone --%>
    <acme:list-column code="spokesperson.milestone.list.label.title" path="title" width="50%"/> [cite: 7]
    <acme:list-column code="spokesperson.milestone.list.label.kind" path="kind" width="30%"/> [cite: 9]
    <acme:list-column code="spokesperson.milestone.list.label.effort" path="effort" width="20%"/> [cite: 7]

    <%-- Atributos ocultos pero disponibles en el modelo de la lista --%>
    <acme:list-hidden path="achievements"/> [cite: 7]
</acme:list>

<%-- Control de creación: Basado en el estado de la campaña --%>
<jstl:if test="${showCreate}"> [cite: 5, 10]
    <acme:button code="spokesperson.milestone.list.button.create" action="/spokesperson/milestone/create?campaignId=${campaignId}"/>
</jstl:if>