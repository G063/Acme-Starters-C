<%@page language="java"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:form-textbox code="any.campaign.label.ticker" path="ticker" readonly="true"/>
    <acme:form-textbox code="any.campaign.label.name" path="name" readonly="true"/>
    <acme:form-textbox code="any.campaign.label.description" path="description" readonly="true"/>
    <acme:form-textbox code="any.campaign.label.startMoment" path="startMoment" readonly="true"/>
    <acme:form-textbox code="any.campaign.label.endMoment" path="endMoment" readonly="true"/>
    <acme:form-textbox code="any.campaign.label.monthsActive" path="monthsActive" readonly="true"/>
    
    <hr/>
    <acme:button code="any.campaign.button.parts" action="/any/milestone/list?campaignId=${id}"/>

    <acme:return/>
</acme:form>