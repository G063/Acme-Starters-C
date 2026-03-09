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
    
    <jstl:if test="${moreInfo != ''}">
        <acme:form-textbox code="any.strategy.label.moreInfo" path="moreInfo" readonly="true"/>
    </jstl:if>
    
    <hr/>
    
    <h3><acme:print code="any.strategy.label.fundraiser-profile"/></h3>

    <acme:form-textbox code="any.strategy.label.fundraiser.name" path="fundraiser.userAccount.identity.name" readonly="true"/>
    <acme:form-textbox code="any.strategy.label.fundraiser.surname" path="fundraiser.userAccount.identity.surname" readonly="true"/>
    <acme:form-textbox code="any.strategy.label.fundraiser.email" path="fundraiser.userAccount.identity.email" readonly="true"/>
    <acme:form-textbox code="any.strategy.label.fundraiser.bank" path="fundraiser.bank" readonly="true"/>
    <acme:form-textarea code="any.strategy.label.fundraiser.statement" path="fundraiser.statement" readonly="true"/>

    <acme:button code="any.strategy.button.tactics" action="/any/tactic/list?strategyId=${id}"/>

    <acme:return/>
</acme:form>