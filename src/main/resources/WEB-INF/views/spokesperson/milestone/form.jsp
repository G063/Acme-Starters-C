<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%-- Determinación del título del formulario según el comando [cite: 7, 8] --%>
<jstl:choose>
    <jstl:when test="${_command == 'create'}">
        <jstl:set var="titleCode" value="spokesperson.milestone.form.title.create"/>
    </jstl:when>
    <jstl:when test="${_command == 'update'}">
        <jstl:set var="titleCode" value="spokesperson.milestone.form.title.update"/>
    </jstl:when>
    <jstl:otherwise>
        <jstl:set var="titleCode" value="spokesperson.milestone.form.title.show"/>
    </jstl:otherwise>
</jstl:choose>

<h3><acme:print code="${titleCode}"/></h3>

<%-- Lógica de control: Solo editable si es creación o si está en borrador [cite: 5, 13] --%>
<jstl:set var="canEdit" value="${_command == 'create' || (draftMode && (_command == 'show' || _command == 'update'))}"/>
<jstl:set var="isReadonly" value="${!canEdit}"/>

<acme:form>
    <%-- Campos principales del Milestone según el modelo [cite: 7] --%>
    <acme:form-textbox code="spokesperson.milestone.form.label.title" path="title" readonly="${isReadonly}"/>
    <acme:form-textarea code="spokesperson.milestone.form.label.achievements" path="achievements" readonly="${isReadonly}"/>
    <acme:form-double code="spokesperson.milestone.form.label.effort" path="effort" readonly="${isReadonly}"/>
    
    <%-- Gestión del enumerado MilestoneKind [cite: 7, 9] --%>
    <jstl:choose>
        <jstl:when test="${!isReadonly}">
            <%-- Nota: 'kinds' debe ser pasado por el servicio como una colección de los valores del enum [cite: 9] --%>
            <acme:form-select code="spokesperson.milestone.form.label.kind" path="kind" choices="${kinds}"/>
        </jstl:when>
        <jstl:otherwise>
            <acme:form-textbox code="spokesperson.milestone.form.label.kind" path="kind" readonly="true"/>
        </jstl:otherwise>
    </jstl:choose>

    <%-- IDs ocultos para mantener el contexto de la campaña o el hito [cite: 5, 7] --%>
    <jstl:if test="${_command == 'update' || _command == 'show' || _command == 'delete'}">
        <input type="hidden" name="id" value="${acme_id}"/>
    </jstl:if>
    
    <jstl:if test="${_command == 'create'}">
        <input type="hidden" name="campaignId" value="${campaignId}"/>
    </jstl:if>

    <hr/>

    <%-- Botonera dinámica basada en permisos y comandos [cite: 5, 10] --%>
    <jstl:choose>
        <%-- Acciones para hitos existentes en modo borrador --%>
        <jstl:when test="${canEdit && _command != 'create'}">
            <acme:submit code="spokesperson.milestone.form.button.update" action="/spokesperson/milestone/update"/>
            <acme:submit code="spokesperson.milestone.form.button.delete" action="/spokesperson/milestone/delete"/>
        </jstl:when>

        <%-- Acción de creación --%>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="spokesperson.milestone.form.button.create" action="/spokesperson/milestone/create"/>
        </jstl:when>
        
        <%-- Botones de acceso rápido desde la vista 'show' si está en borrador --%>
        <jstl:when test="${_command == 'show' && draftMode}">
             <acme:button code="spokesperson.milestone.form.button.update" action="/spokesperson/milestone/update?id=${acme_id}"/>
             <acme:button code="spokesperson.milestone.form.button.delete" action="/spokesperson/milestone/delete?id=${acme_id}"/>
        </jstl:when>
    </jstl:choose>

    <acme:return/>
</acme:form>