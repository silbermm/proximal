describe 'proximal', ->
  controller = undefined
  log = undefined

  beforeEach ->
    module 'proximal'

  describe 'HomeController', ->
    beforeEach(inject ($controller, $log)->
      controller = $controller
      log = $log
      controller = controller('HomeCtrl', {'$log', $log})
    )
    console.log(controller)
    it 'sets the name of the page', ->
      expect(controller.page).toBe('Home Page')

