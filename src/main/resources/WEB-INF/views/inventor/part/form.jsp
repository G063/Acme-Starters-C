<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:choose>
    <jstl:when test="${_command == 'create'}">
        <jstl:set var="titleCode" value="inventor.part.form.title.create"/>
    </jstl:when>
    <jstl:when test="${_command == 'update'}">
        <jstl:set var="titleCode" value="inventor.part.form.title.update"/>
    </jstl:when>
    <jstl:otherwise>
        <jstl:set var="titleCode" value="inventor.part.form.title.show"/>
    </jstl:otherwise>
</jstl:choose>

<h3><acme:print code="${titleCode}"/></h3>

<%-- 
   Lógica corregida: 
   1. Si es CREATE, siempre se puede editar.
   2. Si es SHOW o UPDATE, depende de que draftMode sea true.
--%>
<jstl:set var="canEdit" value="${_command == 'create' || (draftMode && (_command == 'show' || _command == 'update'))}"/>
<jstl:set var="isReadonly" value="${!canEdit}"/>

<acme:form>
    <acme:form-textbox code="inventor.part.form.label.name" path="name" readonly="${isReadonly}"/>
    
    <%-- 
       OJO: En el CreateService debes asegurarte de enviar 'kinds' 
       al response, igual que haces en el Show o Update. 
    --%>
    <jstl:if test="${kinds != null}">
        <acme:form-select code="inventor.part.form.label.kind" path="kind" choices="${kinds}" readonly="${isReadonly}"/>
    </jstl:if>

    <acme:form-textbox code="inventor.part.form.label.cost" path="cost" placeholder="EUR 0.00" readonly="${isReadonly}"/>
    <acme:form-textarea code="inventor.part.form.label.description" path="description" readonly="${isReadonly}"/>

    <%-- Identificadores --%>
    <jstl:if test="${_command == 'update' || _command == 'show'}">
        <input type="hidden" name="id" value="${acme_id}"/>
    </jstl:if>
    
    <jstl:if test="${_command == 'create'}">
        <input type="hidden" name="inventionId" value="${inventionId}"/>
    </jstl:if>

    <hr/>

    <jstl:choose>
        <%-- Botones para UPDATE/DELETE --%>
        <jstl:when test="${canEdit && _command != 'create'}">
            <acme:submit code="inventor.part.form.button.update" action="/inventor/part/update"/>
            <acme:submit code="inventor.part.form.button.delete" action="/inventor/part/delete"/>
        </jstl:when>
        
        <%-- Botón para CREATE --%>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="inventor.part.form.button.create" action="/inventor/part/create"/>
        </jstl:when>
    </jstl:choose>

    <acme:return/>
</acme:form>