/*Anakrime + Adv0cate + Hans211 + Brom + ATX Coder + Gordon + Croner + Warlord*/
typedef HRESULT ( WINAPI* oDrawIndexedPrimitive ) ( LPDIRECT3DDEVICE8 pDevice, D3DPRIMITIVETYPE pType, UINT nMinIndex, UINT nNumVertices, UINT nStartIndex, UINT nPrimitiveCount );
oDrawIndexedPrimitive pDrawIndexedPrimitive;

typedef HRESULT ( WINAPI* oSetStreamSource ) ( LPDIRECT3DDEVICE8 pDevice, UINT nStreamNumber, LPDIRECT3DVERTEXBUFFER8 pStreamData, UINT nStride );
oSetStreamSource pSetStreamSource;

typedef HRESULT ( WINAPI* oPresent ) ( LPDIRECT3DDEVICE8 pDevice, CONST RECT* pSourceRect,CONST RECT* pDestRect,HWND hDestWindowOverride,CONST RGNDATA* pDirtyRegion);
oPresent pPresent;
