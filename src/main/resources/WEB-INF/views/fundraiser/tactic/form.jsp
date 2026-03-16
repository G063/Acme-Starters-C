<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:choose>
    <jstl:when test="${_command == 'create'}">
        <jstl:set var="titleCode" value="fundraiser.tactic.form.title.create"/>
    </jstl:when>
    <jstl:when test="${_command == 'update'}">
        <jstl:set var="titleCode" value="fundraiser.tactic.form.title.update"/>
    </jstl:when>
    <jstl:otherwise>
        <jstl:set var="titleCode" value="fundraiser.tactic.form.title.show"/>
    </jstl:otherwise>
</jstl:choose>

<h3><acme:print code="${titleCode}"/></h3>

<jstl:set var="canEdit" value="${_command == 'create' || (draftMode && (_command == 'show' || _command == 'update'))}"/>
<jstl:set var="isReadonly" value="${!canEdit}"/>

<acme:form>
    <acme:form-textbox code="fundraiser.tactic.form.label.name" path="name" readonly="${isReadonly}"/>
    
    <jstl:if test="${kinds != null}">
        <acme:form-select code="fundraiser.tactic.form.label.kind" path="kind" choices="${kinds}" readonly="${isReadonly}"/>
    </jstl:if>
    
    <acme:form-textbox code="fundraiser.tactic.form.label.expectedPercentage" path="expectedPercentage" placeholder="0.0" readonly="${isReadonly}"/>
    
    <acme:form-textarea code="fundraiser.tactic.form.label.notes" path="notes" readonly="${isReadonly}"/>
    
    <jstl:if test="${_command == 'update' || _command == 'show'}">
        <input type="hidden" name="id" value="${acme_id}"/>
    </jstl:if>
    
    <jstl:if test="${_command == 'create'}">
        <input type="hidden" name="strategyId" value="${strategyId}"/>
    </jstl:if>
    
    <hr/>
    
    <jstl:choose>
        <jstl:when test="${canEdit && _command != 'create'}">
            <acme:submit code="fundraiser.tactic.form.button.update" action="/fundraiser/tactic/update"/>
            <acme:submit code="fundraiser.tactic.form.button.delete" action="/fundraiser/tactic/delete"/>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="fundraiser.tactic.form.button.create" action="/fundraiser/tactic/create"/>
        </jstl:when>
    </jstl:choose>
    
    <acme:return/>
</acme:form>