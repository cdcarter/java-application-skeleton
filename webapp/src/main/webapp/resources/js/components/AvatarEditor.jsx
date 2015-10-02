var util = require("util"),
    $ = require("jquery"),
    React = require("react"),
    noty = require("noty"),
    ReactBootstrap = require("react-bootstrap"),
    ImageCropper = require("./ImageCropper"),
    APIClient = require("../APIClient");

module.exports = React.createClass({

    getDefaultProps: function() {
        return {
            user: null
        }
    },

    getInitialState: function() {
        return {
            cropperOpen: false,
            croppedImg: null,
            img: null
        }
    },

    handleRequestHide: function() {
        console.log("onRequestHide()");
        this.setState({
            cropperOpen: false
        });
    },

    handleCrop: function(dataURI)   {
        console.log("handleCrop");
        this.setState({
            cropperOpen: false,
            img: null,
            croppedImg: dataURI
        });
    },

    componentDidMount: function() {
        var node = React.findDOMNode(this.refs.avatarImage);
        if (node) {
            node.addEventListener('dragover', this.onDragOver);
            node.addEventListener('drop', this.onDrop);
        }
    },

    onDragOver: function(event) {
        event.stopPropagation();
        event.preventDefault();
        event.dataTransfer.dropEffect = 'copy';
    },

    onDrop: function(event) {
        event.stopPropagation();
        event.preventDefault();

        this.openCropperWithFile(event.dataTransfer.files[0]);
    },

    openCropperWithFile: function(file) {
        if (!file) {
            return;
        }

        var reader = new FileReader();
        reader.onload = function(img) {
            this.setState({
                img: img.target.result,
                cropperOpen: true
            });
        }.bind(this);
        reader.readAsDataURL(file);
    },

    render: function() {

        var cropper = this.state.cropperOpen ? <ImageCropper show={true}
                                    onCrop={this.handleCrop}
                                    image={this.state.img}
                                    onRequestHide={this.handleRequestHide} /> : null;

        var avatarImage = this.state.croppedImg ? this.state.croppedImg : APIClient.getLink(this.props.user, "resource-avatar-medium");
        return (
            <div>
                <img ref="avatarImage" src={avatarImage} onDragOver={this.onDragOver} onDrop={this.onDrop} />
                {cropper}
            </div>
        );
    }
});