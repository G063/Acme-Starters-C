<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:set var="titleCode" value="inventor.part.form.title.show"/>

<h3><acme:print code="${titleCode}"/></h3>

<jstl:set var="canEdit" value="${_command == 'create' || (draftMode && (_command == 'show' || _command == 'update'))}"/>
<jstl:set var="isReadonly" value="${!canEdit}"/>

<acme:form>
    <acme:form-textbox code="inventor.part.form.label.name" path="name" readonly="${isReadonly}"/>

    <jstl:if test="${kinds != null}">
        <acme:form-select code="inventor.part.form.label.kind" path="kind" choices="${kinds}" readonly="${isReadonly}"/>
    </jstl:if>

    <acme:form-textbox code="inventor.part.form.label.cost" path="cost" placeholder="EUR 0.00" readonly="${isReadonly}"/>
    <acme:form-textarea code="inventor.part.form.label.description" path="description" readonly="${isReadonly}"/>

    <jstl:if test="${_command == 'update' || _command == 'show'}">
        <input type="hidden" name="id" value="${acme_id}"/>
    </jstl:if>
    
    <jstl:if test="${_command == 'create'}">
        <input type="hidden" name="inventionId" value="${inventionId}"/>
    </jstl:if>

    <hr/>

    <jstl:choose>
        <jstl:when test="${canEdit && _command != 'create'}">
            <acme:submit code="inventor.part.form.button.update" action="/inventor/part/update"/>
            <acme:submit code="inventor.part.form.button.delete" action="/inventor/part/delete"/>
        </jstl:when>

        <jstl:when test="${_command == 'create'}">
            <acme:submit code="inventor.part.form.button.create" action="/inventor/part/create"/>
        </jstl:when>
    </jstl:choose>

</acme:form>