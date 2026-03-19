<%@page language="java"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:form-textbox code="any.campaign.label.ticker" path="ticker" readonly="true"/>
    <acme:form-textbox code="any.campaign.label.name" path="name" readonly="true"/>
    <acme:form-textbox code="any.campaign.label.description" path="description" readonly="true"/>
    <acme:form-textbox code="any.campaign.label.startMoment" path="startMoment" readonly="true"/>
    <acme:form-textbox code="any.campaign.label.endMoment" path="endMoment" readonly="true"/>
    <acme:form-textbox code="any.campaign.label.monthsActive" path="monthsActive" readonly="true"/>
    <acme:form-textbox code="any.campaign.label.effort" path="effort" readonly="true"/>
    
    <hr/>
    <acme:button code="any.campaign.button.milestone" action="/any/milestone/list?campaignId=${id}"/>

    <acme:button code="any.campaign.button.spokesperson" action="/any/spokesperson/show?id=${spokespersonId}&campaignId=${id}"/>
</acme:form>