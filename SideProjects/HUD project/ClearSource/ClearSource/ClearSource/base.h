typedef HRESULT (WINAPI* oReset) (LPDIRECT3DDEVICE8 pDevice, D3DPRESENT_PARAMETERS* pPresentationParameters);
oReset pReset;

typedef HRESULT (WINAPI* oBeginScene) (LPDIRECT3DDEVICE8 pDevice);
oBeginScene pBeginScene;

typedef HRESULT (WINAPI* oEndScene) (LPDIRECT3DDEVICE8 pDevice);
oEndScene pEndScene;

typedef HRESULT (WINAPI* oSetTransform)(LPDIRECT3DDEVICE8 pDevice, D3DTRANSFORMSTATETYPE State, D3DMATRIX* pMatrix);
oSetTransform pSetTransform;

typedef HRESULT ( WINAPI* oDrawIndexedPrimitive ) ( LPDIRECT3DDEVICE8 pDevice, D3DPRIMITIVETYPE pType, UINT nMinIndex, UINT nNumVertices, UINT nStartIndex, UINT nPrimitiveCount );
oDrawIndexedPrimitive pDrawIndexedPrimitive;

typedef HRESULT ( WINAPI* oSetStreamSource ) ( LPDIRECT3DDEVICE8 pDevice, UINT nStreamNumber, LPDIRECT3DVERTEXBUFFER8 pStreamData, UINT nStride );
oSetStreamSource pSetStreamSource;

typedef HRESULT ( WINAPI* oPresent ) ( LPDIRECT3DDEVICE8 pDevice, CONST RECT* pSourceRect,CONST RECT* pDestRect,HWND hDestWindowOverride,CONST RGNDATA* pDirtyRegion);
oPresent pPresent;

typedef HRESULT (WINAPI* oGetTransform) (LPDIRECT3DDEVICE8 pDevice, D3DTRANSFORMSTATETYPE State, D3DMATRIX* pMatrix);
oGetTransform pGetTransform;

typedef HRESULT (WINAPI* oGetViewport) (LPDIRECT3DDEVICE8 pDevice, D3DVIEWPORT8* pViewport);
oGetViewport pGetViewport;

