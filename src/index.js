console.log('Hello, Webpack!');

document.addEventListener('DOMContentLoaded', () => {
    const textInput = document.getElementById('text-input');
    const charCount = document.getElementById('char-count');
    const wordCount = document.getElementById('word-count');
    const lineCount = document.getElementById('line-count');

    const updateCounts = (text) => {
        const charLength = text.length;

        // 単語数（スペースや改行で単語を分割）
        const words = text.trim().match(/\S+/g);
        const wordLength = words ? words.length : 0;

        // 行数 (\nで分割)
        const lines = text.split(/\n/);
        const lineLength = lines.length;

        charCount.textContent = charLength;
        wordCount.textContent = wordLength;
        lineCount.textContent = lineLength;
    };

    textInput.addEventListener('input', (e) => {
        updateCounts(e.target.value);
    });
});