# Single Document Summarizer using Statistical Heuristics

Made this project way back in 2011. This still needs to be published but I think I can safely publish code before publishing it.

It contains the following statistical features to rank sentences - 

- Sentence Location
- Position of Next Sentence when sentences are related to each other
- Use of 'Title Words' in sentences
- Use of words with high frequency
- Use of 'theme words' in sentences
- Use of Proper Nouns
- Use of Cue Phrases
- Use of Topic Segmentation words - one of my favourite features. Makes use of tf.isf (Term Frequency-Inverse Sentence Frequency) and Word Density to find 'Topic' words in document
- Sentence Length
- Use of Punctuation
- Use of numerical data in sentences

Results were compared with DUC (Document Understanding Conference) 2002 summaries and MS Word's Auto Summarizer.


