/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
/******/ (() => { // webpackBootstrap
/******/ 	var __webpack_modules__ = ({

/***/ "./src/index.js":
/*!**********************!*\
  !*** ./src/index.js ***!
  \**********************/
/***/ (() => {

eval("console.log('Hello, Webpack!');\ndocument.addEventListener('DOMContentLoaded', () => {\n  const textInput = document.getElementById('text-input');\n  const charCount = document.getElementById('char-count');\n  const wordCount = document.getElementById('word-count');\n  const lineCount = document.getElementById('line-count');\n  const updateCounts = text => {\n    const charLength = text.length;\n\n    // 単語数（スペースや改行で単語を分割）\n    const words = text.trim().match(/\\S+/g);\n    const wordLength = words ? words.length : 0;\n\n    // 行数 (\\nで分割)\n    const lines = text.split(/\\n/);\n    const lineLength = lines.length;\n    charCount.textContent = charLength;\n    wordCount.textContent = wordLength;\n    lineCount.textContent = lineLength;\n  };\n  textInput.addEventListener('input', e => {\n    updateCounts(e.target.value);\n  });\n});\n\n//# sourceURL=webpack:///./src/index.js?");

/***/ })

/******/ 	});
/************************************************************************/
/******/ 	
/******/ 	// startup
/******/ 	// Load entry module and return exports
/******/ 	// This entry module can't be inlined because the eval devtool is used.
/******/ 	var __webpack_exports__ = {};
/******/ 	__webpack_modules__["./src/index.js"]();
/******/ 	
/******/ })()
;