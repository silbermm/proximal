describe 'header_directive', ->
  element = undefined
  scope = undefined

  beforeEach module 'proximal'

  beforeEach( inject ($rootScope, $compile)->
    scope = $rootScope.$new()
    element = '<prox-header> </prox-header>'
    
    element = $compile(element)(scope)
    scope.$digest()
  )

  it("should be a header element", ->
    expect(element[0].tagName).toBe("HEADER")
  )


