var $ = require("jquery");
var _ = require("underscore");

function APIClient(baseUrl, apiToken) {
    this.baseUrl = baseUrl;
    this.apiToken = apiToken;
}

APIClient.prototype = {
    get: function (resourceUri, options) {
        return $.ajax($.extend(this.getBaseAjaxOptions(resourceUri, "GET"), options));
    },


    patch: function (resourceUri, options) {
        return $.ajax($.extend(this.getBaseAjaxOptions(resourceUri, "PATCH"), options));
    },

    post: function (resourceUri, options) {
        return $.ajax($.extend(this.getBaseAjaxOptions(resourceUri, "POST"), options));
    },

    getRef: function(ref, options) {
        return this.get("", _.extend(options, {url: ref}));
    },

    setAPIToken: function(token) {
        this.apiToken = token;
    },

    getBaseAjaxOptions: function(resourceUri, ajaxMethod) {
        apiOptions = {
            url: this.baseUrl + resourceUri,
            method: ajaxMethod,
            dataType: "json",
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + this.apiToken
            },
            error: function(req, status, errorThrown) {
                console.log("Error encountered for API call: " + req + " (" + status + "): " + errorThrown);
            }
        };
        return apiOptions;
    }

};

APIClient.getLink = function (resource, rel) {
    for (var i = 0; i < resource.links.length; i++) {
        var link = resource.links[i];
        if (link.rel == rel) {
            return link.href;
        }
    }
    return null;
};

module.exports = APIClient;

