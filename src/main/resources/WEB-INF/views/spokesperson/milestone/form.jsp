<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

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

<jstl:set var="canEdit" value="${_command == 'create' || _command == 'update'}"/>
<jstl:set var="isReadonly" value="${!canEdit}"/>

<acme:form>
    <acme:form-textbox code="spokesperson.milestone.form.label.title" path="title" readonly="${isReadonly}"/>
    <acme:form-textarea code="spokesperson.milestone.form.label.achievements" path="achievements" readonly="${isReadonly}"/>
    <acme:form-double code="spokesperson.milestone.form.label.effort" path="effort" readonly="${isReadonly}"/>
    
    <jstl:choose>
        <jstl:when test="${!isReadonly}">
            <acme:form-select code="spokesperson.milestone.form.label.kind" path="kind" choices="${kinds}"/>
        </jstl:when>
        <jstl:otherwise>
            <acme:form-textbox code="spokesperson.milestone.form.label.kind" path="kind" readonly="true"/>
        </jstl:otherwise>
    </jstl:choose>

    <jstl:if test="${_command == 'update' || _command == 'show' || _command == 'delete'}">
        <input type="hidden" name="id" value="${id}"/>
    </jstl:if>
    
    <jstl:if test="${_command == 'create'}">
        <input type="hidden" name="campaignId" value="${campaignId}"/>
    </jstl:if>

    <hr/>

    <jstl:choose>
        <jstl:when test="${canEdit && _command != 'create'}">
            <acme:submit code="spokesperson.milestone.form.button.update" action="/spokesperson/milestone/update"/>
            <acme:submit code="spokesperson.milestone.form.button.delete" action="/spokesperson/milestone/delete"/>
        </jstl:when>

        <jstl:when test="${_command == 'create'}">
            <acme:submit code="spokesperson.milestone.form.button.create" action="/spokesperson/milestone/create"/>
        </jstl:when>
        
        <jstl:when test="${_command == 'show' && draftMode}">
             <acme:button code="spokesperson.milestone.form.button.update" action="/spokesperson/milestone/update?id=${id}"/>
             <acme:submit code="spokesperson.milestone.form.button.delete" action="/spokesperson/milestone/delete"/>
        </jstl:when>
    </jstl:choose>

</acme:form>