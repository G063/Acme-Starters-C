<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>



<jstl:set var="canEdit" value="${_command == 'create' || (sponsorshipDraftMode && (_command == 'show' || _command == 'update'))}"/>
<jstl:set var="isReadonly" value="${!canEdit}"/>

<acme:form>
    <acme:form-textbox code="sponsor.donation.form.label.name" path="name" readonly="${isReadonly}"/>

    <jstl:if test="${kinds != null}">
        <acme:form-select code="sponsor.donation.form.label.kind" path="kind" choices="${kinds}" readonly="${isReadonly}"/>
    </jstl:if>

    <acme:form-textbox code="sponsor.donation.form.label.money" path="money" placeholder="EUR 0.00" readonly="${isReadonly}"/>
    <acme:form-textarea code="sponsor.donation.form.label.notes" path="notes" readonly="${isReadonly}"/>

    <jstl:if test="${_command == 'update' || _command == 'show'}">
        <input type="hidden" name="id" value="${id}"/>
    </jstl:if>

    <jstl:if test="${_command == 'create'}">
        <input type="hidden" name="sponsorshipId" value="${sponsorshipId}"/>
    </jstl:if>

    <hr/>

    <jstl:choose>
        <jstl:when test="${canEdit && _command != 'create'}">
            <acme:submit code="sponsor.donation.form.button.update" action="/sponsor/donation/update"/>
            <acme:submit code="sponsor.donation.form.button.delete" action="/sponsor/donation/delete"/>
        </jstl:when>

        <jstl:when test="${_command == 'create'}">
            <acme:submit code="sponsor.donation.form.button.create" action="/sponsor/donation/create"/>
        </jstl:when>
    </jstl:choose>

</acme:form>
