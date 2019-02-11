var messageApi = Vue.resource('/v1/messages{/id}');
var roomApi = Vue.resource('/v2/room{/roomId}');

Vue.component('message-form', {
    props: ['messages'],
    data: function () {
        return {
            text: '',

        }
    },

    template: '<div >' +
        '<input type="text" placeholder="Text of message" v-model="text"/>' +
        '<input type="button" :class="{button:true}" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function () {
            var message = {text: this.text};

            messageApi.save({}, message).then(result =>
                result.json().then(data => {
                    this.messages.push(data);
                })
            )
        }
    }
});

Vue.component('message-row', {
    props: ['message'],
    template: '<div class="block1"><div>{{message.text}}</div>' +
        '<div class="author"><i>{{message.authorName}}</i></div>' +
        '</div>'
});

Vue.component('messages-list', {
    props: ['messages'],
    template: '<div class="tableChat ">' +
        '<message-form :messages="messages" />' +
        '<message-row v-for="message in messages" :key="message.id" :message="message"/>' +
        '</div>',


});

Vue.component('user-row', {
    props: ['user', 'rooms'],
    template: '<div id="example-2">' +
        ' <button v-on:click="open">{{user.name}}</button>' +
        '</div>',
    methods: {
        open: function (event) {
            var room = {owner: this.owner}
            roomApi.save({}, room).then(result =>
                result.json().then(data => {
                    this.rooms.push(data);
                })
            )

        }
    }


});

Vue.component('users-list', {
    props: ['users'],
    template:
        '<div>' +
        '<h3>You can choose one of users: </h3>' +
        '<user-row v-for="user in users"  :user="user"/>' +
        '</div>'
});

var app = new Vue({
    el: '#app',
    template: '<div>' +

        '<div v-if="!profile"><h4>You can authorize</h4> <a href="/login">here</a> </div>' +
        '<div v-if="!profile"><h4>or add new profile</h4> <a href="/registration">here</a> </div>' +
        '<div v-else> <div>{{profile.name}}&nbsp;<a href="/logout">Log out</a>' +
        '<div><users-list :users="users" /> </div>' +
        '</div></div>' +
        '<messages-list :messages="messages"/>' +

        '</div>',
    data: {
        users: frontendData.users,
        messages: frontendData.messages,
        profile: frontendData.profile
    },
    created: function () {

    }
});