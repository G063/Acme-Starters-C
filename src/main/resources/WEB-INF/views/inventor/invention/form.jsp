<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:set var="isReadonly" value="${_command == 'show'}"/>
<jstl:choose>
    <jstl:when test="${_command == 'create'}">
        <jstl:set var="titleCode" value="inventor.invention.form.title.create"/>
    </jstl:when>
    <jstl:when test="${_command == 'update'}">
        <jstl:set var="titleCode" value="inventor.invention.form.title.update"/>
    </jstl:when>
    <jstl:when test="${_command == 'assign-parts'}">
        <jstl:set var="titleCode" value="inventor.invention.form.title.assign"/>
    </jstl:when>
    <jstl:otherwise>
        <jstl:set var="titleCode" value="inventor.invention.form.title.show"/>
    </jstl:otherwise>
</jstl:choose>

<h3><acme:print code="${titleCode}"/></h3>

<acme:form>
<acme:form-textbox code="inventor.invention.form.label.ticker" path="ticker" readonly="${isReadonly}"/>
    <acme:form-textbox code="inventor.invention.form.label.name" path="name" readonly="${isReadonly}"/>
    <acme:form-textarea code="inventor.invention.form.label.description" path="description" readonly="${isReadonly}"/>
    
    <acme:form-textbox code="inventor.invention.form.label.start-moment" path="startMoment" 
               placeholder="inventor.invention.form.placeholder.date" readonly="${isReadonly}"/>

    <acme:form-textbox code="inventor.invention.form.label.end-moment" path="endMoment" 
               placeholder="inventor.invention.form.placeholder.date" readonly="${isReadonly}"/>

    <jstl:if test="${_command == 'show'}">
        <acme:form-money code="inventor.invention.form.label.cost" path="cost" readonly="true"/>
        <acme:form-textbox code="inventor.invention.form.label.months" path="monthsActive" readonly="true"/>
    </jstl:if>
    
    <acme:form-textbox code="inventor.invention.form.label.more-info" path="moreInfo" readonly="${isReadonly}"/>

    <hr/>

    <jstl:if test="${_command == 'show' && draftMode}">
        <acme:button code="inventor.invention.form.button.navigate" action="/inventor/invention/update?id=${id}"/>
        <acme:submit code="inventor.invention.form.button.delete" action="/inventor/invention/delete"/>
        <acme:submit code="inventor.invention.form.button.publish" action="/inventor/invention/publish?id=${id}"/>
    </jstl:if>
    
    <jstl:if test="${draftMode}">
        <acme:button code="inventor.invention.form.button.manage-parts" 
             action="/inventor/part/list?inventionId=${id}"/>
    </jstl:if>

    <jstl:if test="${_command == 'create'}">
        <acme:submit code="inventor.invention.form.button.create" action="/inventor/invention/create"/>
    </jstl:if>
    
    <jstl:if test="${_command == 'update' && draftMode}">
        <acme:submit code="inventor.invention.form.button.update" action="/inventor/invention/update"/>
    </jstl:if>
    
</acme:form>