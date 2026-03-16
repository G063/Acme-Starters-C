<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:set var="isReadonly" value="${_command == 'show'}"/>

<jstl:choose>
    <jstl:when test="${_command == 'create'}">
        <jstl:set var="titleCode" value="fundraiser.strategy.form.title.create"/>
    </jstl:when>
    <jstl:when test="${_command == 'update'}">
        <jstl:set var="titleCode" value="fundraiser.strategy.form.title.update"/>
    </jstl:when>
    <jstl:otherwise>
        <jstl:set var="titleCode" value="fundraiser.strategy.form.title.show"/>
    </jstl:otherwise>
</jstl:choose>

<h3><acme:print code="${titleCode}"/></h3>

<acme:form>
    
    <acme:form-textbox code="fundraiser.strategy.form.label.ticker" path="ticker" readonly="${isReadonly}"/>
    <acme:form-textbox code="fundraiser.strategy.form.label.name" path="name" readonly="${isReadonly}"/>
    <acme:form-textarea code="fundraiser.strategy.form.label.description" path="description" readonly="${isReadonly}"/>
    
    <acme:form-textbox code="fundraiser.strategy.form.label.start-moment" path="startMoment" 
               placeholder="fundraiser.strategy.form.placeholder.date" readonly="${isReadonly}"/>
    <acme:form-textbox code="fundraiser.strategy.form.label.end-moment" path="endMoment" 
               placeholder="fundraiser.strategy.form.placeholder.date" readonly="${isReadonly}"/>
    
    <jstl:if test="${_command == 'show'}">
        <acme:form-textbox code="fundraiser.strategy.form.label.percentage" path="expectedPercentage" readonly="true"/>
        <acme:form-textbox code="fundraiser.strategy.form.label.months" path="monthsActive" readonly="true"/>
    </jstl:if>
    
    <acme:form-textbox code="fundraiser.strategy.form.label.more-info" path="moreInfo" readonly="${isReadonly}"/>
    
    <hr/>
    
    <jstl:if test="${_command == 'show' && draftMode}">
        <acme:button code="fundraiser.strategy.form.button.navigate" action="/fundraiser/strategy/update?id=${id}"/>
        <acme:submit code="fundraiser.strategy.form.button.delete" action="/fundraiser/strategy/delete"/>
    </jstl:if>
    
    <jstl:if test="${_command == 'create'}">
        <acme:submit code="fundraiser.strategy.form.button.create" action="/fundraiser/strategy/create"/>
    </jstl:if>
    
    <jstl:if test="${_command == 'update' && draftMode}">
        <acme:button code="fundraiser.strategy.form.button.manage-tactics" action="/fundraiser/tactic/list?strategyId=${id}"/>
        <acme:submit code="fundraiser.strategy.form.button.update" action="/fundraiser/strategy/update"/>
        <acme:submit code="fundraiser.strategy.form.button.publish" action="/fundraiser/strategy/publish"/>
    </jstl:if>
    
    <acme:return/>
</acme:form>