var util = require("util"),
    $ = require("jquery"),
    React = require("react"),
    Progress = require("react-progress"),
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
            imageReadProgress: null,
            img: null
        }
    },

    handleRequestHide: function() {
        this.setState({
            cropperOpen: false,
            img: null
        });
    },

    handleCrop: function(dataURI)   {
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

    onFileSelection: function(event) {
        this.openCropperWithFile(event.target.files[0]);
    },

    openCropperWithFile: function(file) {
        if (!file) {
            return;
        }

        var reader = new FileReader();

        reader.onload = function(img) {
            this.setState({
                img: img.target.result,
                cropperOpen: true,
                imageReadProgress: null
            });
        }.bind(this);

        reader.onprogress = function(event) {
            var percentComplete = Math.round(event.loaded / event.total * 100);
            this.setState({
                imageReadProgress: percentComplete
            });
        }.bind(this);

        reader.readAsDataURL(file);
    },

    openFileSelector: function() {
        React.findDOMNode(this.refs.avatarFileInput).click();
    },

    render: function() {
        return (
            <div className="avatar-editor">
                <input ref="avatarFileInput"
                       type="file"
                       className="hidden"
                       accept="image/*"
                       onChange={this.onFileSelection} />
                <img className="img-rounded"
                     ref="avatarImage"
                     src={this.getAvatarImage()}
                     onDragOver={this.onDragOver}
                     onDrop={this.onDrop}
                     onClick={this.openFileSelector} />
                {this.renderImageReadProgress()}
                {this.renderImageCropper()}
            </div>
        );
    },

    renderImageCropper: function() {
        if (!this.state.cropperOpen) {
            return null;
        }

        return (
            <ImageCropper show={true}
                          width={250}
                          height={250}
                          onCrop={this.handleCrop}
                          image={this.state.img}
                          onRequestHide={this.handleRequestHide}/>
        )
    },

    renderImageReadProgress: function() {
        if (!this.state.imageReadProgress) {
            return null;
        }

        return <progress value={this.state.imageReadProgress} max="100"></progress>;
    },

    getAvatarImage: function() {
        if (this.state.croppedImg) {
            return this.state.croppedImg;
        }
        return APIClient.getLink(this.props.user, "resource-avatar-medium");
    }
});
