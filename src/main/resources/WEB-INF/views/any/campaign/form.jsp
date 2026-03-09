<%@page language="java"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">

    <acme:form-textbox path="ticker" code="any.campaign.form.ticker"/>
    <acme:form-textbox path="name" code="any.campaign.form.name"/>

    <acme:form-textarea path="description" code="any.campaign.form.description"/>

    <acme:form-moment path="startMoment" code="any.campaign.form.startMoment"/>
    <acme:form-moment path="endMoment" code="any.campaign.form.endMoment"/>

</acme:form>