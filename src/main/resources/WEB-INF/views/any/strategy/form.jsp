<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:form-textbox code="any.strategy.label.ticker" path="ticker" readonly="true"/>
    <acme:form-textbox code="any.strategy.label.name" path="name" readonly="true"/>
    <acme:form-textbox code="any.strategy.label.startMoment" path="startMoment" readonly="true"/>
    <acme:form-textbox code="any.strategy.label.endMoment" path="endMoment" readonly="true"/>
    <acme:form-textbox code="any.strategy.label.monthsActive" path="monthsActive" readonly="true"/>
    <acme:form-textbox code="any.strategy.label.expectedPercentage" path="expectedPercentage" readonly="true"/>
    <acme:form-textarea code="any.strategy.label.description" path="description" readonly="true"/>
    
    <jstl:if test="${moreInfo != null && !moreInfo.isEmpty()}">
        <acme:form-textbox code="any.strategy.label.moreInfo" path="moreInfo" readonly="true"/>
    </jstl:if>
    
    <hr/>

    <acme:button code="any.strategy.button.tactics" action="/any/tactic/list?strategyId=${id}"/>
    
    <acme:button code="any.strategy.button.fundraiser" action="/any/fundraiser/show?id=${fundraiserId}&strategyId=${id}"/>

</acme:form>