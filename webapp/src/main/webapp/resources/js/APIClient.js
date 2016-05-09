var $ = require("jquery"),
    _ = require("lodash"),
    uuid = require("uuid-v4");

function APIClient(baseUrl, apiToken) {
    this.baseUrl = baseUrl;
    this.apiToken = apiToken;
}

APIClient.prototype = {
    get: function (resourceUri, options) {
        return this.ajax(resourceUri, "GET", options);
    },

    patch: function (resourceUri, options) {
        return this.ajax(resourceUri, "PATCH", options);
    },

    post: function (resourceUri, options) {
        return this.ajax(resourceUri, "POST", options);
    },

    getRef: function(ref, options) {
        return this.get("", _.extend(options, {url: ref}));
    },

    ajax: function(resourceUri, method, options) {
        var jqXHR = $.ajax($.extend(this.getBaseAjaxOptions(resourceUri, method), options));
        jqXHR.requestId = uuid();
        return jqXHR;
    },

    setAPIToken: function(token) {
        this.apiToken = token;
    },

    getBaseAjaxOptions: function(resourceUri, ajaxMethod) {
        let apiOptions = {
            url: this.baseUrl + resourceUri,
            method: ajaxMethod,
            dataType: "json",
            contentType: "application/json",
            headers: this.getHeaders(),
            error: function(req, status, errorThrown) {
                console.log("Error encountered for API call: " + req + " (" + status + "): " + errorThrown);
            }
        };
        return apiOptions;
    },

    getHeaders: function() {
        return {
            'Authorization': 'Bearer ' + this.apiToken
        };
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

APIClient.parseSort = function(sortString) {
    var sorts = {};
    sortString = sortString || "";
    var sortParts = sortString.split(",");
    for (var i = 0; i < sortParts.length; i++) {
        var sortPieces = sortParts[i].split(":");
        sorts[sortPieces[0]] = sortPieces[1];
    }
    return sorts;
};

APIClient.collapseQuery = function(query) {
    return _.omit(query, function(value) {
        return _.isUndefined(value) || _.isNull(value) || value === '';
    });
};

module.exports = APIClient;

