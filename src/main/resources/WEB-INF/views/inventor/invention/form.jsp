<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:set var="canEdit" value="${_command == 'create' || draftMode}"/>
<jstl:set var="isReadonly" value="${!canEdit}"/>

<jstl:set var="titleCode" value="inventor.invention.form.title.show"/>

<h3><acme:print code="${titleCode}"/></h3>

<acme:form>
    <acme:form-textbox code="inventor.invention.form.label.ticker" path="ticker" readonly="${isReadonly}"/>
    <acme:form-textbox code="inventor.invention.form.label.name" path="name" readonly="${isReadonly}"/>
    <acme:form-textarea code="inventor.invention.form.label.description" path="description" readonly="${isReadonly}"/>
    
    <acme:form-textbox code="inventor.invention.form.label.start-moment" path="startMoment" 
               placeholder="inventor.invention.form.placeholder.date" readonly="${isReadonly}"/>

    <acme:form-textbox code="inventor.invention.form.label.end-moment" path="endMoment" 
               placeholder="inventor.invention.form.placeholder.date" readonly="${isReadonly}"/>

    <jstl:if test="${_command != 'create'}">
        <acme:form-money code="inventor.invention.form.label.cost" path="cost" readonly="true"/>
        <acme:form-textbox code="inventor.invention.form.label.months" path="monthsActive" readonly="true"/>
    </jstl:if>
    
    <acme:form-textbox code="inventor.invention.form.label.more-info" path="moreInfo" readonly="${isReadonly}"/>
	
    <jstl:if test="${_command != 'create'}">
        <input type="hidden" name="id" value="${id}"/>
    </jstl:if>

    <hr/>

    <jstl:choose>
        <jstl:when test="${canEdit && _command != 'create'}">
            <acme:submit code="inventor.invention.form.button.update" action="/inventor/invention/update"/>
            <acme:submit code="inventor.invention.form.button.delete" action="/inventor/invention/delete"/>
            <acme:submit code="inventor.invention.form.button.publish" action="/inventor/invention/publish"/>
            <acme:button code="inventor.invention.form.button.manage-parts" 
             action="/inventor/part/list?inventionId=${id}"/>
        </jstl:when>

        <jstl:when test="${_command == 'create'}">
            <acme:submit code="inventor.invention.form.button.create" action="/inventor/invention/create"/>
        </jstl:when>
    </jstl:choose>
    
    <jstl:if test="${_command != 'create' && !canEdit}">
    <acme:button code="inventor.invention.form.button.show-parts" 
             action="/inventor/part/list?inventionId=${id}"/>
	</jstl:if>
    
</acme:form>